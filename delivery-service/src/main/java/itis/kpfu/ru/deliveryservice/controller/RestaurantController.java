package itis.kpfu.ru.deliveryservice.controller;

import itis.kpfu.ru.deliveryservice.util.FileUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/public/v1/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final JobLauncher jobLauncher;
    private final Job exportOrderReportJob;

    @GetMapping("/{restaurantId}/order/week-report")
    @ResponseStatus(HttpStatus.OK)
    public void deliverOrder(@PathVariable("restaurantId") Long restaurantId, HttpServletResponse response) throws Exception {
        var filePath = Paths.get("order_report_%s.csv".formatted(UUID.randomUUID())).toAbsolutePath().toString();
        JobParameters jobParameters = new JobParametersBuilder()
            .addLong("time", System.currentTimeMillis())
            .addString("filePath", filePath)
            .addLong("restaurantId", restaurantId)
            .toJobParameters();

        jobLauncher.run(exportOrderReportJob, jobParameters);

        FileUtils.downloadLocalFileToHttpResponse(response, filePath);
    }
}
