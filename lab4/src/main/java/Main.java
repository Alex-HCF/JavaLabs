import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    @Data
    @AllArgsConstructor
    static class Division {
        private long id;
        private String name;
    }

    @Data
    @Builder
    static class Human {
        private long id;
        private String name;
        private String gender;
        private LocalDate BirthDate;
        private Division division;
        private int salary;
    }

    public static void main(String[] args) throws IOException, CsvValidationException {

        var path = "src/main/resources/foreign_names.csv";
        var separator = ';';

        List<Human> humans = new ArrayList<>();

        // key - name of the division (G, J, O...)
        Map<String, Division> divisionMap = new HashMap<>();

        try (FileReader fileReader = new FileReader(path);
             CSVReader csvReader = new CSVReaderBuilder(fileReader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(separator).build())
                     .withSkipLines(1)
                     .build()) {

            String[] lineValues;

            while ((lineValues = csvReader.readNext()) != null) {

                Division division = divisionMap.get(lineValues[4]);
                if (division == null) {
                    division = divisionMap.put(lineValues[4], new Division(divisionMap.size(), lineValues[4]));
                }

                Human human = Human.builder()
                        .id(humans.size())
                        .name(lineValues[1])
                        .gender(lineValues[2])
                        .BirthDate(LocalDate.parse(lineValues[3], DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                        .division(division)
                        .salary(Integer.parseInt(lineValues[5]))
                        .build();

                humans.add(human);
            }
        }

        humans.forEach(System.out::println);
    }

}
