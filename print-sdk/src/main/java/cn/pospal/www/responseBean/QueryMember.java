package cn.pospal.www.responseBean;

import java.io.Serializable;

import cn.pospal.www.requestBean.MemberInfo;

/**
 * Created by jinchangsheng on 18/8/1.
 */

public class QueryMember extends BaseResponse implements Serializable{
    MemberInfo memberInfo;

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }
}
