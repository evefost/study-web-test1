package com.big.data.controller;


import com.big.data.kafka.Msg;
import com.big.data.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/kafka")
public class KafkaController {

	private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);
	@Resource
	private KafkaProducerService kafkaService;
	
	@RequestMapping("/test")
	@ResponseBody
	public String test(){
		logger.info("-------KafkaController--------start-----");
		System.err.println("---------KafkaController--------start---------");
		kafkaService.sendDefaultInfo("kafka sendMessage test with spring!");
		return "index";
	}

	@RequestMapping("/test2")
	@ResponseBody
	public String test2(){
		logger.info("-------KafkaController--------start-----");
		System.err.println("---------KafkaController--------start---------");
		kafkaService.sendMesage("test2","测试2");
		return "index";
	}

	@RequestMapping("/test3")
	@ResponseBody
	public String test3(){
		logger.info("-------KafkaController--------start-----");
		System.err.println("---------KafkaController--------start---------");
		kafkaService.sendMesage("test3","测试3");
		return "index";
	}

	@RequestMapping("/test4")
	@ResponseBody
	public String test4(){
		logger.info("-------KafkaController--------start-----");
		System.err.println("---------KafkaController--------start---------");
		Msg msg = new Msg();
		msg.setMessage("测试四");
		kafkaService.sendMesage("test4",msg);
		return "index";
	}
}
