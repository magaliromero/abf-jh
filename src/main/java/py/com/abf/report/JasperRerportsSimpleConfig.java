package py.com.jogapo.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasperRerportsSimpleConfig {

    @Bean
    public SimpleReportFiller reportFiller() {
        return new SimpleReportFiller();
    }

    @Bean
    public SimpleReportExporter reportExporter() {
        return new SimpleReportExporter();
    }
}
