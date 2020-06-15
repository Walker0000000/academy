package com.academy.utils;

import com.academy.enums.ResultStatusCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 移动端api接口返回的数据模型
 * @author
 *
 */
@Slf4j
public class ResultUtil {

	/**
	 * 返回结果状态值key
	 */
	private static final Integer CODE_S = 0;

	private static final Integer CODE_E = -1;

	private int code;		//返回的代码，0表示成功，其他表示失败

	/**
	 * 数据总条数
	 */
	private Long count = 0L;


    private String msg;		//成功或失败时返回的错误信息

    private Object data;	//成功时返回的数据信息

	public ResultUtil(int code, String msg, Object data){
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResultUtil(ResultStatusCode resultStatusCode, Object data){
		this(resultStatusCode.getCode(), resultStatusCode.getMsg(), data);
	}

	public ResultUtil(int code, String msg){
		this(code, msg, null);
		log.info("----- Shiro认证-----"+msg);
	}

	public ResultUtil(ResultStatusCode resultStatusCode){
		this(resultStatusCode, null);
		log.info("----- Shiro认证-----"+resultStatusCode.getMsg());
	}

	public ResultUtil(){

	}

	/**
	 * 成功
	 * @return
	 */
	public static ResultUtil success() {
		ResultUtil result = new ResultUtil();
		result.setCode(CODE_S);
		result.setMsg("操作成功");
		log.info("操作成功");
		result.setData("");
		return result;
	}

	/**
	 * 成功
	 *
	 * @param data
	 *            数据
	 * @return
	 */
	public static ResultUtil success(Object data) {
		ResultUtil result = new ResultUtil();
		result.setCode(CODE_S);
		result.setMsg("操作成功");
		log.info("操作成功");
		result.setData(data);
		return result;
	}


	/**
	 * 分页查询成功
	 *
	 * @param data
	 *            数据
	 * @return
	 */
	public static ResultUtil success(Object data,long count) {
		ResultUtil result = new ResultUtil();
		result.setCode(CODE_S);
		result.setMsg("操作成功");
        result.setCount(count);
		log.info("操作成功");
		result.setData(data);
		return result;
	}

	/**
	 * 失败
	 *
	 * @return
	 */
	public static ResultUtil error() {
		ResultUtil result = new ResultUtil();
		result.setCode(CODE_E);
		result.setMsg("系统繁忙");
		log.info("系统错误---"+"系统繁忙");
		return result;
	}

	/**
	 * session 过期
	 *
	 * @return
	 */
	public static ResultUtil error(int code,String msg) {
		ResultUtil result = new ResultUtil();
		result.setCode(code);
		result.setMsg(msg);
		log.info("系统错误---"+"系统繁忙");
		return result;
	}

	/**
	 * 失败
	 *
	 * @return
	 */
	public static ResultUtil error( String msg ) {
		ResultUtil result = new ResultUtil();
		result.setCode(CODE_E);
		result.setMsg(msg);
		log.info("系统错误---"+msg);
		return result;
	}

    public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}

}
