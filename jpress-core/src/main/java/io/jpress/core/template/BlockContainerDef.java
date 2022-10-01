package io.jpress.core.template;

import java.util.*;

/**
 * Foreocal container definition of template
 */
public class BlockContainerDef {

    // container ID
    private String id;

    // The template file is located
    private String templateFile;

    // Support stored sector ID List
    private Set<String> supportBlocks;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }


    public Set<String> getSupportBlocks() {
        return supportBlocks;
    }


    public void setSupportBlocks(Set<String> supportBlocks) {
        this.supportBlocks = supportBlocks;
    }


    public void addSupportBlockId(String blockId) {
        if (this.supportBlocks == null) {
            this.supportBlocks = new HashSet<>();
        }
        this.supportBlocks.add(blockId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockContainerDef containerDef = (BlockContainerDef) o;
        return Objects.equals(id, containerDef.id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Map<String, Object> toBsFormData() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", getId());
        data.put("tag", "container");
        data.put("templateFile", getTemplateFile());
        data.put("supportBlocks", getSupportBlocks());
        return data;
    }
}
