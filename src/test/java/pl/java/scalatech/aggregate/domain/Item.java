package pl.java.scalatech.aggregate.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class Item {
    private String reqId;
    private String name;
    
    private MetaInformation meta;

   
  
}
