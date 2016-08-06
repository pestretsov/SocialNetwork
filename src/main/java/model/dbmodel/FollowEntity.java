package model.dbmodel;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by artemypestretsov on 7/23/16.
 */

@Data
public class Follow {
    // followerId is following userId;
    @NotNull
    private int followerId;

    @NotNull
    private int userId;
}
