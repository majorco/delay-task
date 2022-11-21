package com.majorco.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据库实体类
 * </p>
 *
 * @author xxxiao
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("delay_task")
@Builder
public class DelayTask implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键id
   */
  @TableId(value = "id", type = IdType.INPUT)
  private Long id;

  /**
   * 任务名字
   */
  private String taskName;
  /**
   * 任务描述
   */
  private String taskDescription;
  /**
   * 创建时间
   */
  private LocalDateTime createTime;
  /**
   * 更新时间,如果任务没有正常结束,数据库将为null
   */
  private LocalDateTime updateTime;

  /**
   * 任务指定的执行时间
   */
  private LocalDateTime invokeTime;
  /**
   * 是否已经执行
   */
  private Boolean isInvoked;
  /**
   * 是否是执行失败的任务
   */
  private Boolean isFailed;
  /**
   * 逻辑删除,未使用mp插件,不能自动拼接 is_delete=false
   */
  private Boolean isDelete;
  /**
   * 类信息
   */
  private String classInfo;


}
