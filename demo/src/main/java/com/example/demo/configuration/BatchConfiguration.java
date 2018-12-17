package com.example.demo.configuration;

import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.batch.JobListener;
import com.example.demo.batch.Transaction;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {
	
	@Value("${batch.reader.name}")
	private String source;
	
	@Value("${batch.target.name}")
	private String target;
	
	@Value("${batch.chunk.size}")
	private int chuckSize;

    @Bean
    public ItemReader<Transaction> reader(){

        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(source));
        reader.setLineMapper(new DefaultLineMapper<Transaction>(){
            {
                setLineTokenizer(new DelimitedLineTokenizer(){
                    {
                        setNames(new String[]{"id","username","content","createdate"});
                    }
                    
                    {
                    	setDelimiter(",");
                    }
                    
                    {
                    	setStrict(false);
                    }
                });

                setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaction>(){
                    {
                        setTargetType(Transaction.class);
                    }
                });
            }
        });
        return reader;
    }
    
    @Bean
    public ItemWriter<Transaction> writer(){
    	
    	FlatFileItemWriter<Transaction> writer = new FlatFileItemWriter<>();
    	writer.setResource(new FileSystemResource(target));
    	DelimitedLineAggregator<Transaction> lineAggregator = new DelimitedLineAggregator<Transaction>();
    	lineAggregator.setDelimiter(",");
    	BeanWrapperFieldExtractor<Transaction> fieldExtractor = new BeanWrapperFieldExtractor<Transaction>();
    	String[] names = {"username","content"};
    	fieldExtractor.setNames(names);
    	lineAggregator.setFieldExtractor(fieldExtractor);
    	writer.setLineAggregator(lineAggregator);
    	return writer;
    	
    }
    
    @Bean
    public JobRepository cronjobRepository() throws Exception {
    	MapJobRepositoryFactoryBean jobRepositoryFactoryBean = new MapJobRepositoryFactoryBean();
        return jobRepositoryFactoryBean.getObject();
    }
    
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public SimpleJobLauncher cronjobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(cronjobRepository());
        return jobLauncher;
    }
    
    @Bean JobListener jobListener(){
    	return new JobListener();
    }

    @Bean
    public Job job(JobBuilderFactory jobs,Step s){
        return jobs.get("job")
                .incrementer(new RunIdIncrementer())
                .flow(s)
                .end()
                .listener(jobListener())
                .build();
    }

    @Bean
    public Step step(StepBuilderFactory stepBuilderFactory,ItemReader<Transaction> reader,ItemWriter<Transaction> writer){
        return stepBuilderFactory.get("s")
                .<Transaction,Transaction>chunk(chuckSize)
                .reader(reader)
                .writer(writer)
                .build();
    }
    
    @Override
    public void setDataSource(DataSource dataSource) {
    	
    }

}