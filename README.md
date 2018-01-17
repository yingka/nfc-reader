# nfc-reader
android nfc reader
支持读取的nfc标签格式：ISO 14443-3A、ISO 14443-3B、JIS 6319-4、ISO 15693、ISO 14443-4、Ndef、NdefFormatable、MifareClassic、MifareUltralight
使用前台调度系统：
NFC前台调度系统是一种用于在运行的程序中（前台呈现的Activity）处理Tag的技术，即前台调度系统允许Activity拦截Intent对象，并且声明该Activity的优先级比
其他的处理Intent对象的Activity高。前台调度系统在一些涉及需要在前台呈现的页面中直接获取或推送NFC信息时十分方便。本文的示例就是使用前台调度。
