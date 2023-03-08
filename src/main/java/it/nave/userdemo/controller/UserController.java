package it.nave.userdemo.controller;

import it.nave.userdemo.dto.UserDto;
import it.nave.userdemo.dto.UserFilter;
import it.nave.userdemo.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "user")
public class UserController {

    private final UserService userService;
    private final JobLauncher jobLauncher;
    private final Job job;

    public UserController(UserService userService, JobLauncher jobLauncher, Job job) {
        this.userService = userService;
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @GetMapping(path = "{id}")
    public UserDto read(@PathVariable long id) {
        return userService.read(id);
    }

    @PutMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long id, @RequestBody UserDto dto) {
        dto.setId(id);
        userService.update(dto);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }

    @GetMapping
    public Page<UserDto> filter(@ParameterObject UserFilter filter, @ParameterObject Pageable pageable) {
        return userService.filter(filter, pageable);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importCsv(@RequestPart("file") MultipartFile file) throws JobExecutionException, IOException {
        Path csvFile = Files.createTempFile("import", ".csv");
        file.transferTo(csvFile);
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("csv-file", csvFile.toAbsolutePath().toString())
                .toJobParameters());
    }
}
