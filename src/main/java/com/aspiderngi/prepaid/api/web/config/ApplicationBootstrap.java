package com.aspiderngi.prepaid.api.web.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationBootstrap implements WebApplicationInitializer {

	@Override
 	public void onStartup(ServletContext container) throws ServletException {

		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ApplicationConfiguration.class);
		rootContext.register(ApplicationAuthenticationSecurityConfiguration.class);
		rootContext.register(NCCPICCSCD1ClientConfiguration.class);
		rootContext.register(NCCPICCSCD3ClientConfiguration.class);		
		rootContext.setServletContext(container);
 
		container.addListener(new ContextLoaderListener(rootContext));

		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(rootContext));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/rest/*");
		servlet.addMapping("/admin/*");
		// add another mapping like /admin/
		container.addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class)
				 .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false,  "/rest/*","/admin/*");
		 
	}
}