package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.ReadLogModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReadLogMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(ReadLogModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return ReadLogModel
     */
    ReadLogModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(ReadLogModel record);

    /**
     * 获取作者的阅读量
     * @param parentId
     * @param analyst
     * @param articleTypeList
     * @param startTime
     * @param endTime
     * @return
     */
    Integer selectArticleAuthorReadCount(@Param("parentId") String parentId,
                                         @Param("analyst") String analyst,
                                         @Param("articleTypeList") List<String> articleTypeList,
                                         @Param("startTime") Date startTime,
                                         @Param("endTime") Date endTime);
}