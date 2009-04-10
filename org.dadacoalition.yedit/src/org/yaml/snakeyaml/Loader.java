/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.util.Iterator;

import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.nodes.*;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Loader {
    protected final BaseConstructor constructor;
    protected Resolver resolver;
    private boolean attached = false;

    public Loader(BaseConstructor constructor) {
        super();
        this.constructor = constructor;
        this.resolver = new Resolver();
    }

    public Loader() {
        this(new Constructor());
    }
    
    public BaseConstructor getConstructor(){
    	return this.constructor;
    }

    public Object load(java.io.Reader io) {
        Composer composer = new Composer(new ParserImpl(new Reader(io)), resolver);
        constructor.setComposer(composer);
        return constructor.getSingleData();
    }

    public Iterable<Object> loadAll(java.io.Reader yaml) {
        Composer composer = new Composer(new ParserImpl(new Reader(yaml)), resolver);
        this.constructor.setComposer(composer);
        Iterator<Object> result = new Iterator<Object>() {
            public boolean hasNext() {
                return constructor.checkData();
            }

            public Object next() {
                return constructor.getData();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return new YamlIterable(result);
    }

    public Iterable<Node> loadAll2(java.io.Reader yaml) {
        Composer composer = new Composer(new ParserImpl(new Reader(yaml)), resolver);
        this.constructor.setComposer(composer);
        Iterator<Node> result = new Iterator<Node>() {
            public boolean hasNext() {
                return constructor.checkData();
            }

            public Node next() {
                return constructor.getRootNode();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return new YamlIterable2(result);
    }    

    private class YamlIterable2 implements Iterable<Node> {
        private Iterator<Node> iterator;

        public YamlIterable2(Iterator<Node> iterator) {
            this.iterator = iterator;
        }

        public Iterator<Node> iterator() {
            return iterator;
        }

    }    
    
    private class YamlIterable implements Iterable<Object> {
        private Iterator<Object> iterator;

        public YamlIterable(Iterator<Object> iterator) {
            this.iterator = iterator;
        }

        public Iterator<Object> iterator() {
            return iterator;
        }

    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Because Loader is stateful it cannot be shared
     */
    void setAttached() {
        if (!attached) {
            attached = true;
        } else {
            throw new YAMLException("Loader cannot be shared.");
        }
    }
}
