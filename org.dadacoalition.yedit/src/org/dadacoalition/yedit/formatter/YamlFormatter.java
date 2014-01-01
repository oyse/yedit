package org.dadacoalition.yedit.formatter;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlFormatter {

    private final DumperOptions options;
    
    private YamlFormatter(Builder builder) {
        options = new DumperOptions();
        options.setExplicitStart(builder.explicitStart);
        options.setExplicitEnd(builder.explicitEnd);
        options.setIndent(builder.indent);
        options.setWidth(builder.lineLength);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);        
    }
    
    public static class Builder {
        private boolean explicitStart = false;
        private boolean explicitEnd = false;
        private int indent = 2;
        private int lineLength = 80;
        
        public Builder(){
            
        }
        
        public Builder explicitStart(boolean explicitStart){
            this.explicitStart = explicitStart; 
            return this;
        }
        
        public Builder explicitEnd(boolean explicitEnd){
            this.explicitEnd = explicitEnd; 
            return this;
        }

        public Builder indent(int indent){
            this.indent = indent; 
            return this;
        }

        public Builder lineLength(int lineLength){
            this.lineLength = lineLength; 
            return this;
        }
        
        
        public YamlFormatter build() {
            return new YamlFormatter(this);
        }
    }
    
    
    public String formatDocument(Object document) {
        
        Yaml yaml = new Yaml(options);
        
        if( null == document ){
            return "---" + System.lineSeparator() + "...";
        }
        return yaml.dump(document).trim();
        
    }
    
}
