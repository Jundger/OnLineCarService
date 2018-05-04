package com.jundger.work.pojo;

public class RecordCode {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column record_code.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column record_code.record_id
     *
     * @mbggenerated
     */
    private Integer recordId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column record_code.code_id
     *
     * @mbggenerated
     */
    private Integer codeId;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table record_code
     *
     * @mbggenerated
     */
    public RecordCode(Integer id, Integer recordId, Integer codeId) {
        this.id = id;
        this.recordId = recordId;
        this.codeId = codeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table record_code
     *
     * @mbggenerated
     */
    public RecordCode() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column record_code.id
     *
     * @return the value of record_code.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column record_code.id
     *
     * @param id the value for record_code.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column record_code.record_id
     *
     * @return the value of record_code.record_id
     *
     * @mbggenerated
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column record_code.record_id
     *
     * @param recordId the value for record_code.record_id
     *
     * @mbggenerated
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column record_code.code_id
     *
     * @return the value of record_code.code_id
     *
     * @mbggenerated
     */
    public Integer getCodeId() {
        return codeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column record_code.code_id
     *
     * @param codeId the value for record_code.code_id
     *
     * @mbggenerated
     */
    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }
}