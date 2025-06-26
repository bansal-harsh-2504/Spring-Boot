package net.harsh.journalApp.repository;

import net.harsh.journalApp.entity.JournalEntryv2;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntryv2, ObjectId> {

}