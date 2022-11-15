package com.majorco.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.majorco.entity.DelayTask;
import com.majorco.mapper.DelayTaskMapper;
import com.majorco.service.DelayTaskRepository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxxiao
 * @since 2022-11-12
 */
@Service
public class DelayTaskRepositoryImpl extends ServiceImpl<DelayTaskMapper, DelayTask> implements
    DelayTaskRepository {

}
