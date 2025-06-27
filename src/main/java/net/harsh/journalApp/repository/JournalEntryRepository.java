package net.harsh.journalApp.repository;

import net.harsh.journalApp.entity.JournalEntryv2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntryv2, ObjectId> {
    public List<JournalEntryv2> findByUserId(ObjectId userId);
}