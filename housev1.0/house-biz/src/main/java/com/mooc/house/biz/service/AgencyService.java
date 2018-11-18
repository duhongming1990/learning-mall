package com.mooc.house.biz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mooc.house.biz.mapper.AgencyMapper;
import com.mooc.house.common.model.Agency;
import com.mooc.house.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {

  @Autowired
  private AgencyMapper agencyMapper;

  @Value("${file.prefix}")
  private String imgPrefix;

  /**
   * 访问user表获取详情 添加用户头像
   * 
   * @param userId
   * @return
   */
  public User getAgentDeail(Long userId) {
    User user = new User();
    user.setId(userId);
    user.setType(2);
    List<User> list = agencyMapper.selectAgent(user);
    setImg(list);
    if (!list.isEmpty()) {
      User agent = list.get(0);
      //将经纪人关联的经纪机构也一并查询出来
      Agency agency = new Agency();
      agency.setId(agent.getAgencyId());
      List<Agency> agencies = agencyMapper.select(agency);
      if (!agencies.isEmpty()) {
          agent.setAgencyName(agencies.get(0).getName());
      }
      return agent;
    }
    return null;
  }

  private void setImg(List<User> list) {
    list.forEach(i -> {
      i.setAvatar(imgPrefix + i.getAvatar());
    });

  }

  public PageInfo<User> getAllAgent(User agency) {
    List<User> agents = agencyMapper.selectAgent(new User());
    setImg(agents);
    PageHelper.startPage(agency.getPageNum(),agency.getPageSize());
    PageInfo pageInfo = new PageInfo(agents);
    return pageInfo;
  }

  public Agency getAgency(Long id) {
    Agency agency = new Agency();
    agency.setId(id);
    List<Agency> agencies = agencyMapper.select(agency);
    if (agencies.isEmpty()) {
      return null;
    }
    return agencies.get(0);
  }

  public List<Agency> getAllAgency() {
    return agencyMapper.select(new Agency());
  }
  
  public int add(Agency agency) {
    return  agencyMapper.insert(agency);
  }

  public void sendMsg(User agent, String msg, String name, String email) {
    // TODO Auto-generated method stub
    
  }

}
