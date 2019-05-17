package it.unisalento.db.crud.DbMongo.repository;

import it.unisalento.db.crud.DbMongo.domain.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRepository extends MongoRepository<Test, String> {

    //@Query("select * from test")
    //@Query(" {position: { $geoWithin: { $box:  [ [ 41, 12 ], [ 43, 13 ] ] }}} ")
    //public List<Test> findAllBySquare();

    @Query(" {position: { $geoWithin: { $box:  [ [ ?0, ?1 ], [ ?2, ?3 ] ] }}} ")
    public List<Test> findAllBySquare(@Param("latMin") double latMin,@Param("lonMin") double lonMin,@Param("latMax") double latMax,@Param("lonMax") double lonMax);

    public List<Test> findAll(Sort sort);

    //List<Test> findAllBySquare(double lonMin, double latMin, double lonMax, double latMax, int bucket);
}
