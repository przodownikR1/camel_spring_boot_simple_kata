package pl.java.scalatech.nbp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.apache.camel.Body;
import org.apache.camel.Headers;

import com.google.common.io.CharStreams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public  class FileContentLoader{
    public void customLoad(@Body InputStream inputStream, @Headers Map<String,Object> header) throws IOException {
        log.info("+++          customLoad  {}",header);
        String text = null;
        try (final Reader reader = new InputStreamReader(inputStream)) {
            text = CharStreams.toString(reader);
            
        }
        log.info("+++ content_part {}",text);
    }
    }