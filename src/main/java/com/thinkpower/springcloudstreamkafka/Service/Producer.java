package com.thinkpower.springcloudstreamkafka.Service;

import com.thinkpower.springcloudstreamkafka.Interface.MyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding({MyProcessor.class})
//@EnableBinding({PolledProcessor.class})
public class Producer {
    private MyProcessor mySource;

    @Autowired
    public Producer(MyProcessor mySource) {
        super();
        this.mySource = mySource;
    }

    public MyProcessor getMysource() {
        return mySource;
    }

    public void setMysource(MyProcessor mysource) {
        mySource = mySource;
    }
}

