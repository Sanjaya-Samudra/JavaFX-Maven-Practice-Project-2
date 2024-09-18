package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item {
    private String itemCode;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Integer qtyOnHand;
}