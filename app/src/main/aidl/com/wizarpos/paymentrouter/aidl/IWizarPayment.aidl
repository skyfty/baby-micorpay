package com.wizarpos.paymentrouter.aidl;
interface IWizarPayment{
    String payCash(String data);
    String doReverse(String jsonData);
    String transact(String jsonData);
    String getPayInfo(String jsonData);
	String getPOSInfo(String jsonData);
	String getYunAccountServerInfo(String jsonData);
	String login(String jsonData);
	String initKey(String jsonData);
	String settle(String jsonData);
	String printLast(String jsonData);
	String consumeCancel(String jsonData);
	String balanceQuery(String jsonData);
	String payCashWithSign(String jsonData);
	String doReverseWithSign(String jsonData);
}