/**
 * 
 */
package com.ruoogle.base;

/**
 * @author shicong
 *
 */
public interface IActivityResManager {
	
	abstract void resInit();//初始化资源数据
	abstract void resRelease();//释放资源
	abstract void resRestore();//恢复资源

}
