package com.trun.fun.common.enums;

public enum OrgOperateIDEnum implements ICodeEnum{
	ADD(41,"新增"), MOD(42,"修改"), DEL(43,"删除");

	private Integer code;
	private String message;

    private OrgOperateIDEnum(int code,String message) {
        this.code = code;
        this.message = message;
    }
    
    public static OrgOperateIDEnum getByCode(int index) {
    	for(OrgOperateIDEnum orgOperateIDEnum : values()) {
    		if(orgOperateIDEnum.getCode() == index) {
    			return orgOperateIDEnum;
    		}
    	}
    	return null;
    }

	@Override
	public Integer getCode() {
		return this.code;
	}
    
    
}
