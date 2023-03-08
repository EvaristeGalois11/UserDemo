package it.nave.userdemo.config;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.entity.User;
import it.nave.userdemo.mapper.UserMapper;
import it.nave.userdemo.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;

@Configuration
public class BatchConfig {
    @Bean
    @StepScope
    public FlatFileItemReader<UserDto> itemReader(@Value("#{jobParameters['csv-file']}") String path) {
        return new FlatFileItemReaderBuilder<UserDto>()
                .name("csvReader")
                .linesToSkip(1)
                .resource(new FileSystemResource(path))
                .delimited()
                .names("name", "surname", "mail", "address")
                .targetType(UserDto.class)
                .build();
    }

    @Bean
    public ItemProcessor<UserDto, User> itemProcessor(UserMapper userMapper) {
        return userMapper::toEntity;
    }

    @Bean
    public ItemWriter<User> itemWriter(UserRepository userRepository) {
        return new RepositoryItemWriterBuilder<User>()
                .repository(userRepository)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<UserDto> itemReader,
                     ItemProcessor<UserDto, User> itemProcessor,
                     ItemWriter<User> itemWriter) {
        return new StepBuilder("step", jobRepository)
                .<UserDto, User>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("job", jobRepository)
                .start(step)
                .build();
    }
}
