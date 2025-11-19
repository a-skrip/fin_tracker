package com.skillbox.data.repository;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.model.Analytic;
import com.skillbox.model.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AnalyticRepositoryImpl implements AnalyticRepository {

    private final String fileName;
    private final ObjectMapper objectMapper;

    public AnalyticRepositoryImpl(String fileName) {
        this.fileName = fileName;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Analytic.class, new AnalyticCustomSerializer());
        this.objectMapper.registerModule(module);

        this.objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
    }


    @Override
    public void save(Analytic analytic) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(this.fileName), analytic);
            System.out.println("JSON файл успешно создан!");
        } catch (IOException e) {
            System.err.println("Ошибка доступа к файлу" + e.getMessage());
        }
    }
    private static class AnalyticCustomSerializer extends JsonSerializer<Analytic> {

        @Override
        public void serialize(Analytic analytic, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            generator.writeStartObject();

            generator.writeStringField("calculationDate", analytic.getCalculationDate().format(DateTimeFormatter.BASIC_ISO_DATE));
            generator.writeStringField("filterDescription", analytic.getFilterDescription());
            generator.writeStringField("groupOption", analytic.getGroupOption());
            generator.writeStringField("aggregateOption", analytic.getAggregateOption());
            generator.writeFieldName("analytic");
            if (analytic.getAggregateOption()==null) {
                Map<String, List<Transaction>> data = analytic.getData();
                generator.writeStartObject();
                for (Map.Entry<String, List<Transaction>> entry : data.entrySet()) {
                    generator.writeFieldName(entry.getKey());
                    generator.writeStartArray();
                    for (Transaction tr : entry.getValue()) {
                        generator.writeString(tr.getAmount().toString());
                    }
                    generator.writeEndArray();
                }
                generator.writeEndObject();
            } else {
                Map<String, BigDecimal> aggregateData = analytic.getAggregateData();
                generator.writeStartObject();
                for (Map.Entry<String, BigDecimal> entry : aggregateData.entrySet()) {
                    generator.writeNumberField(entry.getKey(), entry.getValue());
                }
                generator.writeEndObject();
            }
            generator.writeEndObject();
        }
    }
}
