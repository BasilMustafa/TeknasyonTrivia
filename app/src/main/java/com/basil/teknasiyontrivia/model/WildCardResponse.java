
package com.basil.teknasiyontrivia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WildCardResponse {

    @SerializedName("wildcard_num")
    @Expose
    private Integer wildcardNum;
    @SerializedName("total_questions")
    @Expose
    private Integer totalQuestions;

    public Integer getWildcardNum() {
        return wildcardNum;
    }

    public void setWildcardNum(Integer wildcardNum) {
        this.wildcardNum = wildcardNum;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

}
