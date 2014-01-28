package com.ilearnrw.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;

import ilearnrw.resource.ResourceLoader;

public class SpringResourceLoader extends ResourceLoader implements
		ResourceLoaderAware {

	org.springframework.core.io.ResourceLoader loader;
	
	@Value("${data.folder}")
	private String dataFolder;

	public SpringResourceLoader() throws Exception {
		ResourceLoader.setResourceLoaderInstance(this);
	}

	@Override
	public InputStream getInputStream(Type type, String resource) {

		switch (type) {
		case DATA:
			resource = dataFolder + resource;
			break;
		}

		Resource r = loader.getResource(resource);
		try {
			if (!r.exists())
				throw new FileNotFoundException(resource);
			else {
				return r.getInputStream();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void setResourceLoader(
			org.springframework.core.io.ResourceLoader resourceLoader) {
		this.loader = resourceLoader;
	}

}
