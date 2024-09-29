package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.SplittableRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetail {

    private String orderId;

    private String itemCode;

    private Integer qty;

    private Double discount;
}
