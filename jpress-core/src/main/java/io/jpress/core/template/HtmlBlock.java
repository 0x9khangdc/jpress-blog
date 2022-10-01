package io.jpress.core.template;

import io.jboot.utils.StrUtil;
import io.jpress.core.bsformbuilder.BsFormComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * html Block, or a html fragment
 * One HtmlBlock Can be converted to bsFormComponent
 */
public class HtmlBlock {

    //Template component
    public static final String DRAG_TYPE_TEMPLATE = "template";

    //System built -in component
    public static final String DRAG_TYPE_SYSTEM = "system";

    //Layout component
    public static final String DRAG_TYPE_LAYOUT = "layout";


    // id
    protected String id;

    //type
    protected String type = DRAG_TYPE_TEMPLATE;

    //Component name
    protected String name;

    //icon
    protected String icon;

    //Sort
    protected String index;

    //Template content
    protected String template;

    //Support configuration content
    protected List<HtmlBlockOptionDef> optionDefs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return StrUtil.isNotBlank(name) ? name : id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void addOptionDef(HtmlBlockOptionDef optionDef) {
        if (optionDefs == null) {
            optionDefs = new ArrayList<>();
        }

        HtmlBlockOptionDef existOptionDef = null;
        for (HtmlBlockOptionDef def : optionDefs) {
            if (Objects.equals(def.getName(), optionDef.getName())) {
                existOptionDef = def;
            }
        }

        if (existOptionDef == null) {
            optionDefs.add(optionDef);
        } else {
            mergeOptionDef(optionDef, existOptionDef);
        }
    }

    /**
     * Merged option To exist options
     *
     * @param newOptionDef
     * @param existOptionDef
     */
    private void mergeOptionDef(HtmlBlockOptionDef newOptionDef, HtmlBlockOptionDef existOptionDef) {
        if (StrUtil.isNotBlank(newOptionDef.getType())) {
            existOptionDef.setType(newOptionDef.getType());
        }

        if (StrUtil.isNotBlank(newOptionDef.getDefaultValue())) {
            existOptionDef.setDefaultValue(newOptionDef.getDefaultValue());
        }
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        TemplateUtil.readAndFillHtmlBlock(template, this);
    }

    public void addTemplateLine(String line) {
        if (template == null) {
            template = "";
        }
        template += line;
    }


    public BsFormComponent toBsFormComponent() {

        BsFormComponent component = new BsFormComponent();
        component.setName(getName());
        component.setTag(id);
        component.setDragIcon(icon);
        component.setDragTitle(getName());
        component.setDragIndex(index);
        component.setDragType(type);
        component.setTemplate(getTemplate());

        if (optionDefs != null) {
            for (HtmlBlockOptionDef optionDef : optionDefs) {
                component.addProp(optionDef.toBsFormComponentPorp());
            }
        }

        return component;
    }

}
