// package com.bknife.orm.config;

// import javax.sql.DataSource;

// import org.springframework.beans.factory.InjectionPoint;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.config.ConfigurableBeanFactory;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.context.ConfigurableApplicationContext;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Scope;
// import org.springframework.core.ResolvableType;

// import com.bknife.orm.OrmConstants;
// import com.bknife.orm.annotion.DBDataSource;
// import com.bknife.orm.assemble.MysqlAssembleFactory;
// import com.bknife.orm.assemble.SqlAssembleFactory;
// import com.bknife.orm.mapper.Mapper;
// import com.bknife.orm.mapper.MapperFactory;
// import com.bknife.orm.mapper.MapperFactoryImpl;
// import com.bknife.orm.mapper.MapperService;

// @Configuration
// @ConditionalOnClass({ Mapper.class, MapperService.class })
// @EnableConfigurationProperties(OrmProperties.class)
// public class OrmAutoConfiguration implements OrmConstants {
//     @Autowired
//     private OrmProperties ormProperties;

//     @Autowired(required = false)
//     private DataSource dataSource;

//     @Bean
//     @ConditionalOnMissingBean(OrmConfig.class)
//     public OrmConfig ormConfig() {
//         String type = ormProperties.getType() == null ? ORM_MYSQL : ormProperties.getType();
//         boolean verbose = ormProperties.isVerbose();
//         OrmConfig config = new OrmConfig();
//         config.setType(type);
//         config.setVerbose(verbose);
//         return config;
//     }

//     @Bean
//     @ConditionalOnMissingBean(SqlAssembleFactory.class)
//     public SqlAssembleFactory sqlAssembleFactory(OrmConfig ormConfig) throws Exception {
//         if (ORM_MYSQL.equalsIgnoreCase(ormConfig.getType()))
//             return new MysqlAssembleFactory();
//         throw new IllegalArgumentException("orm type is illegal value");
//     }

//     @Bean
//     @ConditionalOnMissingBean(MapperFactory.class)
//     public MapperFactory mapperFactory(SqlAssembleFactory factory, OrmConfig ormConfig) {
//         return new MapperFactoryImpl(factory, ormConfig.isVerbose());
//     }

//     @Bean
//     @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//     public <T> Mapper<T> createMapper(final ConfigurableApplicationContext context, final InjectionPoint ip,
//             final MapperFactory factory)
//             throws Exception {
//         return factory.createMapperByType(getMapperClass(context, ip), getDataSource(context, ip));
//     }

//     @Bean
//     @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//     public <T> MapperService<T> createMapperService(final ConfigurableApplicationContext context,
//             final InjectionPoint ip,
//             final MapperFactory factory) throws Exception {
//         return factory.createServiceByType(getMapperClass(context, ip), getDataSource(context, ip));
//     }

//     private DataSource getDataSource(final ConfigurableApplicationContext context, final InjectionPoint ip)
//             throws Exception {
//         DBDataSource dbDataSource = ip.getAnnotation(DBDataSource.class);
//         DataSource currentDataSource;

//         if (dbDataSource != null)
//             currentDataSource = (DataSource) context.getBean(dbDataSource.value());
//         else
//             currentDataSource = dataSource;
//         if (currentDataSource == null)
//             throw new NullPointerException("dataSource is null");
//         return currentDataSource;
//     }

//     @SuppressWarnings("unchecked")
//     private <T> Class<T> getMapperClass(final ConfigurableApplicationContext context, final InjectionPoint ip)
//             throws Exception {

//         ResolvableType resolvableType = null;
//         if (ip.getField() != null)
//             resolvableType = ResolvableType.forField(ip.getField());
//         else if (ip.getMethodParameter() != null)
//             resolvableType = ResolvableType.forMethodParameter(ip.getMethodParameter());
//         if (resolvableType == null)
//             throw new Exception("cannot found table generic type");
//         return (Class<T>) resolvableType.getGeneric(0).resolve();
//     }
// }
