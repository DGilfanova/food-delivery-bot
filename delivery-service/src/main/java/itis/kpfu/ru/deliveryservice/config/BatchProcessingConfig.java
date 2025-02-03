package itis.kpfu.ru.deliveryservice.config;

import itis.kpfu.ru.deliveryservice.job.report.CsvFileWriter;
import itis.kpfu.ru.deliveryservice.job.report.model.OrderReport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class BatchProcessingConfig {

    private static final Integer BATCH_SIZE = 10;

    @Bean
    public Job exportOrderReportJob(Step exportOrderReportStep, JobRepository jobRepository) {
        return new JobBuilder("exportOrderReportJob", jobRepository)
            .start(exportOrderReportStep)
            .build();
    }

    @Bean
    public Step step(ItemProcessor<OrderReport, OrderReport> processor, JobRepository jobRepository,
                     PlatformTransactionManager transactionManager, CsvFileWriter csvFileWriter, DataSource dataSource) throws Exception {
        return new StepBuilder("exportOrderReportStep", jobRepository)
            .<OrderReport, OrderReport>chunk(BATCH_SIZE, transactionManager)
            .reader(pagingItemReader(dataSource))
            .processor(processor)
            .writer(csvFileWriter)
            .build();
    }

    @Bean
    public JdbcPagingItemReader<OrderReport> pagingItemReader(DataSource dataSource) throws Exception {
        JdbcPagingItemReader<OrderReport> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setQueryProvider(queryProvider(dataSource).getObject());

        reader.setRowMapper((rs, rowNum) -> OrderReport.builder()
            .orderId(rs.getLong("order_id"))
            .totalPrice(rs.getObject("total_price", BigDecimal.class))
            .status(rs.getString("status"))
            .createdAt(rs.getObject("created_at", LocalDateTime.class))
            .dish(rs.getString("dish"))
            .dishPrice(rs.getObject("dish_price", BigDecimal.class))
            .dishQuantity(rs.getObject("dish_quantity", Long.class))
            .restaurantName(rs.getString("restaurant_name"))
            .courierName(rs.getString("courier_name"))
            .courierLastName(rs.getString("courier_last_name"))
            .build()
        );

        reader.setPageSize(100);

        return reader;
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean queryProvider(DataSource dataSource) {
        var provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);

        provider.setSelectClause("""
            SELECT o.id         as order_id,
                   total_price,
                   status,
                   o.created_at as created_at,
                   d.name       as dish,
                   d.price      as dish_price,
                   oi.quantity  as dish_quantity,
                   r.name       as restaurant_name,
                   c.first_name as courier_name,
                   c.last_name  as courier_last_name
            """);

        provider.setFromClause("""
            FROM orders o
                     LEFT JOIN order_item oi ON o.id = oi.order_id
                     LEFT JOIN dish d ON oi.dish_id = d.id
                     LEFT JOIN restaurant r ON o.restaurant_id = r.id
                     LEFT JOIN courier c ON o.courier_id = c.id
            """);

        provider.setWhereClause("restaurant_id = 1 AND o.created_at > '%s'".formatted(LocalDateTime.now().minusWeeks(1).toString()));

        provider.setSortKey("order_id");

        return provider;
    }
}
