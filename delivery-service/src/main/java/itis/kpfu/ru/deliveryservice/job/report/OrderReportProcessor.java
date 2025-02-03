package itis.kpfu.ru.deliveryservice.job.report;

import itis.kpfu.ru.deliveryservice.job.report.model.OrderReport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderReportProcessor implements ItemProcessor<OrderReport, OrderReport> {

    @Override
    public OrderReport process(OrderReport item) {
        if (item.getTotalPrice().doubleValue() > 100) {
            item.setDiscountPrice(BigDecimal.valueOf(item.getTotalPrice().doubleValue() * 0.9));
        }
        return item;
    }
}
