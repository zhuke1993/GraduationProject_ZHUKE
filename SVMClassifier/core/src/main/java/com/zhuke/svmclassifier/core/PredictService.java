package com.zhuke.svmclassifier.core;

import libsvm.svm;
import libsvm.svm_node;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预测线程，不断预测传来的数据
 *
 * @author ZHUKE
 */
@Service
public class PredictService implements Runnable {
    private Logger logger = LogManager.getLogger(PredictService.class);

    @Autowired
    private MessageSendService messageSendService;

    public void predict() {
        Thread th = null;
        logger.info("..........预测线程已启动..........");

        while (true) {
            // 从缓存数组中取出相应的行，进行预测
            for (int i = (SVMConfig.TEMP_STATE - SVMConfig.L), j = 0; i < (SVMConfig.TEMP_STATE - SVMConfig.R); i++, j++) {
                SVMConfig.TO_PERDICT[j] = SVMConfig.ACTION_TEMP[i + 20 + 1];
            }
            // 将待预测数组进行格式化处理，将各属性值进行调整
            double[] t = new double[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];

            for (int k = 0; k < SVMConfig.FEATURE_NUM; k++) {
                for (int j = 0; j < SVMConfig.R - SVMConfig.L; j++) {
                    t[j * SVMConfig.FEATURE_NUM + k] = SVMConfig.ACTION_TEMP[j][k];
                }
            }
            // 将该数组填充进node数组，进行预测
            svm_node[] nodes = new svm_node[(SVMConfig.R - SVMConfig.L) * SVMConfig.FEATURE_NUM];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new svm_node();
                nodes[i].index = i + 1;
                nodes[i].value = t[i];
            }
            // 进行预测,得出预测值
            //double result = svm.svm_predict(SVMConfig.MODEL, nodes);

            double result = 1.0000;

            // 向局域网内的所有的家电广播得到的结果值
            messageSendService.setMessage(String.valueOf(result));
            messageSendService.sendMessage();
            try {
                Thread.sleep(SVMConfig.PREDICT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        predict();
    }
}
