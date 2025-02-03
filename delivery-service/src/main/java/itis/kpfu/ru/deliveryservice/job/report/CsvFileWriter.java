package itis.kpfu.ru.deliveryservice.job.report;

import itis.kpfu.ru.deliveryservice.job.report.model.OrderReport;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
@StepScope
public class CsvFileWriter implements ItemWriter<OrderReport> {

    @Value("#{jobParameters['filePath']}")
    private String filePath;

    private boolean isHeaderWritten = false;

    @Override
    public void write(Chunk<? extends OrderReport> chunk) throws Exception {
        if (filePath == null) {
            filePath = "order_report.csv";
        }

        try (FileWriter writer = new FileWriter(filePath, true)) {
            if (!isHeaderWritten) {
                writer.write("Order ID,Total Price,Discount Price,Status,Created At,Dish,Dish Price,Dish Quantity,Restaurant Name,Courier Name,Courier Last Name\n");
                isHeaderWritten = true;
            }

            for (OrderReport item : chunk) {
                writer.write(
                    item.getOrderId() + "," +
                        item.getTotalPrice() + "," +
                        item.getDiscountPrice() + "," +
                        item.getStatus() + "," +
                        item.getCreatedAt() + "," +
                        item.getDish() + "," +
                        item.getDishPrice() + "," +
                        item.getDishQuantity() + "," +
                        item.getRestaurantName() + "," +
                        item.getCourierName() + "," +
                        item.getCourierLastName() + "\n"
                );
            }
        } catch (IOException e) {
            throw new Exception("Ошибка при записи в файл: " + filePath, e);
        }
    }
}
