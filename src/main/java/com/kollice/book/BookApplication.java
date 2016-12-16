package com.kollice.book;

import com.kollice.book.entity.AuthorSettings;
import com.kollice.book.entity.CustomPermissionsAuthorizationFilter;
import com.kollice.book.framework.base.CustomRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication
@EnableWebMvc
@EnableTransactionManagement
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
public class BookApplication extends WebMvcConfigurerAdapter implements TransactionManagementConfigurer {
    /* 以下是事务配置，支持多数据源事务 start */
    @Resource(name="transactionManager")
    private PlatformTransactionManager txManager;

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    /* 以下是事务配置，支持多数据源事务 end */

    /* 以下是自定义配置，配置信息在author.properties文件中 start */
    @Autowired
    private AuthorSettings authorSettings;

    public void setAuthorSettings(AuthorSettings authorSettings) {
        this.authorSettings = authorSettings;
    }
    /* 以下是自定义配置，配置信息在author.properties文件中 end */

    /* 以下是视图配置 start */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/page/");
        resolver.setSuffix(".html");
        return resolver;
    }
    /* 以下是视图配置 end */

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
