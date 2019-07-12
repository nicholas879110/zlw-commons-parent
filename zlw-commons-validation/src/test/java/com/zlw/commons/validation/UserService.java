package com.zlw.commons.validation;

import java.util.List;

import com.zlw.commons.core.ResultData;
import com.zlw.commons.validation.annotation.Min;
import com.zlw.commons.validation.annotation.NotNull;
import com.zlw.commons.validation.annotation.Range;
import com.zlw.commons.validation.annotation.RegMatcher;
import org.springframework.stereotype.Component;

import com.zlw.commons.validation.annotation.NotEmpty;
import com.zlw.commons.validation.annotation.Validation;


@Component
public class UserService {

	@Validation(only="username")
	public ResultData<Object> save(User user , @NotNull(message="user2 not null") @NotEmpty(message="save.user 2 不允许空") @Min(value="10")  String user2){
		System.out.println(  "===========验证Entiy + 单个参数================" );
		return ResultData.newSuccessResultData();
	}
	
	@Validation(ignore="reg")
	public ResultData<Object> save( User user , User2 user2){
		System.out.println(  "===========验证多个Entiy ================" );
		return ResultData.newSuccessResultData();
	}
	
	@Validation
	public ResultData<Object> save( @NotNull(message="user2 不允许为空")  String user2){
		System.out.println(  "===========验证单个参数================" );
		return ResultData.newSuccessResultData();
	}
	
	@Validation
	public ResultData<Object> save( 
			@NotNull(message="userId不能为空")Long userId,
			Long id,
			@NotNull(message="limit不能为空") @Range(min="0",max="100",message="limit有效范围是0~100")Integer limit ,
			@NotNull(message="flush不能为空") @RegMatcher(regex = "^[0-1]{1}+$",message="flush只能是0和1")Integer flush){
		System.out.println(  "===========正则表达式验证================" );
		return ResultData.newSuccessResultData();
	}
	
	
	@Validation
    public ResultData<List<Object>> getUserNewsByPageList(@NotNull(message="userId不能为空")Long userId, 
                Long id,
                @NotNull(message="limit不能为空") @Range(min="0",max="100",message="limit有效范围是0~100")Integer limit,
                @NotNull(message="flush不能为空") @RegMatcher(regex = "^[0-1]{1}+$",message="flush只能是0和1")Integer flush) {
		return ResultData.newSuccessResultData();
    }
	
}
