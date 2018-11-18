package com.mooc.house.biz.mapper;

import com.mooc.house.common.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    public List<House>  selectPageHouses(@Param("house")House house);

	public int insert(User account);

	public List<Community> selectCommunity(Community community);

	public int insert(House house);

	public HouseUser selectHouseUser(@Param("userId")Long userId,@Param("id") Long houseId,@Param("type") Integer integer);
	
	public HouseUser selectSaleHouseUser(@Param("id") Long houseId);

	public int insertHouseUser(HouseUser houseUser);

	public int insertUserMsg(UserMsg userMsg);

	public int updateHouse(House updateHouse);
	
	public  int downHouse(Long id);

	public int deleteHouseUser(@Param("id")Long id,@Param("userId") Long userId,@Param("type") Integer value);
	
}
