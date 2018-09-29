package com.jundger.work.pojo.JPush;

import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 16:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Audience {

    private List<String> tag;
    private List<String> tag_and;
    private List<String> tag_not;
    private List<String> alias;
    private List<String> registration_id;
    private List<String> segment;
    private List<String> abtest;

    public Audience(List<String> tag, List<String> alias, List<String> registration_id) {
        this.tag = tag;
        this.alias = alias;
        this.registration_id = registration_id;
    }

    public Audience(List<String> tag, List<String> tag_and, List<String> tag_not, List<String> alias, List<String> registration_id) {
        this.tag = tag;
        this.tag_and = tag_and;
        this.tag_not = tag_not;
        this.alias = alias;
        this.registration_id = registration_id;
    }

    public Audience(List<String> tag, List<String> tag_and, List<String> tag_not, List<String> alias, List<String> registration_id, List<String> segment, List<String> abtest) {
        this.tag = tag;
        this.tag_and = tag_and;
        this.tag_not = tag_not;
        this.alias = alias;
        this.registration_id = registration_id;
        this.segment = segment;
        this.abtest = abtest;
    }

    public List<String> getTag() {
        return tag;
    }

    public List<String> getTag_and() {
        return tag_and;
    }

    public List<String> getTag_not() {
        return tag_not;
    }

    public List<String> getAlias() {
        return alias;
    }

    public List<String> getRegistration_id() {
        return registration_id;
    }

    public List<String> getSegment() {
        return segment;
    }

    public List<String> getAbtest() {
        return abtest;
    }
}
