package org.fitnesse.jbehave;

import fitnesse.testsystems.Descriptor;
import fitnesse.testsystems.TestSystem;
import fitnesse.testsystems.TestSystemFactory;
import fitnesse.wiki.SystemVariableSource;
import fitnesse.wikitext.parser.VariableSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class JBehaveTestSystemFactory implements TestSystemFactory {

    private VariableSource variableSource;

    public JBehaveTestSystemFactory(Properties properties) {
        this(new SystemVariableSource(properties));
    }

    public JBehaveTestSystemFactory(VariableSource variableSource) {
        this.variableSource = variableSource;
    }

    @Override
    public TestSystem create(Descriptor descriptor) throws IOException {
        URLClassLoader classLoader = new URLClassLoader(getUrlsFromClassPath(descriptor), getClass().getClassLoader());
        return new JBehaveTestSystem(descriptor.getTestSystem(), classLoader, variableSource);
    }

    private URL[] getUrlsFromClassPath(Descriptor descriptor) throws MalformedURLException {
        String[] paths = descriptor.getClassPath().split(System.getProperty("path.separator"));
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            urls[i] = new File(path).toURI().toURL();
        }
        return urls;
    }
}