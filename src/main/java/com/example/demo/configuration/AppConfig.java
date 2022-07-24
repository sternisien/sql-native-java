package com.example.demo.configuration;

import com.example.demo.dto.ProprietaireDto;
import com.example.demo.entities.Proprietaire;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapEmptyString;
import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapNull;

@Configuration
public class AppConfig {

  /**
   * Function to get an instance of Mapper
   *
   * @return Mapper initialize
   */
  @Bean
  public Mapper getMapper() {
    return DozerBeanMapperBuilder.create().withMappingBuilder(beanMappingBuilder()).build();
  }

  /**
   * Function to configure the mapper to avoid exception when the one attribute is null or not
   * mapping the empty value
   *
   * @return {@link BeanMappingBuilder} managing the configuration of mapper
   */
  private BeanMappingBuilder beanMappingBuilder() {
    return new BeanMappingBuilder() {
      @Override
      protected void configure() {
        mapping(ProprietaireDto.class, Proprietaire.class, mapNull(false), mapEmptyString(false));
      }
    };
  }
}
