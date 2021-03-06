package me.khrystal.gmatorm.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import me.khrystal.gmatorm.ormdto.VocabularyCategory;
import me.khrystal.gmatorm.ormdto.VocabularyDeck;
import me.khrystal.gmatorm.ormdto.VocabularyDeckFlashcard;
import me.khrystal.gmatorm.ormdto.VocabularyFlashcard;
import me.khrystal.gmatorm.ormdto.VocabularyGeneralData;

import me.khrystal.gmatorm.greendao.VocabularyCategoryDao;
import me.khrystal.gmatorm.greendao.VocabularyDeckDao;
import me.khrystal.gmatorm.greendao.VocabularyDeckFlashcardDao;
import me.khrystal.gmatorm.greendao.VocabularyFlashcardDao;
import me.khrystal.gmatorm.greendao.VocabularyGeneralDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig vocabularyCategoryDaoConfig;
    private final DaoConfig vocabularyDeckDaoConfig;
    private final DaoConfig vocabularyDeckFlashcardDaoConfig;
    private final DaoConfig vocabularyFlashcardDaoConfig;
    private final DaoConfig vocabularyGeneralDataDaoConfig;

    private final VocabularyCategoryDao vocabularyCategoryDao;
    private final VocabularyDeckDao vocabularyDeckDao;
    private final VocabularyDeckFlashcardDao vocabularyDeckFlashcardDao;
    private final VocabularyFlashcardDao vocabularyFlashcardDao;
    private final VocabularyGeneralDataDao vocabularyGeneralDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        vocabularyCategoryDaoConfig = daoConfigMap.get(VocabularyCategoryDao.class).clone();
        vocabularyCategoryDaoConfig.initIdentityScope(type);

        vocabularyDeckDaoConfig = daoConfigMap.get(VocabularyDeckDao.class).clone();
        vocabularyDeckDaoConfig.initIdentityScope(type);

        vocabularyDeckFlashcardDaoConfig = daoConfigMap.get(VocabularyDeckFlashcardDao.class).clone();
        vocabularyDeckFlashcardDaoConfig.initIdentityScope(type);

        vocabularyFlashcardDaoConfig = daoConfigMap.get(VocabularyFlashcardDao.class).clone();
        vocabularyFlashcardDaoConfig.initIdentityScope(type);

        vocabularyGeneralDataDaoConfig = daoConfigMap.get(VocabularyGeneralDataDao.class).clone();
        vocabularyGeneralDataDaoConfig.initIdentityScope(type);

        vocabularyCategoryDao = new VocabularyCategoryDao(vocabularyCategoryDaoConfig, this);
        vocabularyDeckDao = new VocabularyDeckDao(vocabularyDeckDaoConfig, this);
        vocabularyDeckFlashcardDao = new VocabularyDeckFlashcardDao(vocabularyDeckFlashcardDaoConfig, this);
        vocabularyFlashcardDao = new VocabularyFlashcardDao(vocabularyFlashcardDaoConfig, this);
        vocabularyGeneralDataDao = new VocabularyGeneralDataDao(vocabularyGeneralDataDaoConfig, this);

        registerDao(VocabularyCategory.class, vocabularyCategoryDao);
        registerDao(VocabularyDeck.class, vocabularyDeckDao);
        registerDao(VocabularyDeckFlashcard.class, vocabularyDeckFlashcardDao);
        registerDao(VocabularyFlashcard.class, vocabularyFlashcardDao);
        registerDao(VocabularyGeneralData.class, vocabularyGeneralDataDao);
    }
    
    public void clear() {
        vocabularyCategoryDaoConfig.clearIdentityScope();
        vocabularyDeckDaoConfig.clearIdentityScope();
        vocabularyDeckFlashcardDaoConfig.clearIdentityScope();
        vocabularyFlashcardDaoConfig.clearIdentityScope();
        vocabularyGeneralDataDaoConfig.clearIdentityScope();
    }

    public VocabularyCategoryDao getVocabularyCategoryDao() {
        return vocabularyCategoryDao;
    }

    public VocabularyDeckDao getVocabularyDeckDao() {
        return vocabularyDeckDao;
    }

    public VocabularyDeckFlashcardDao getVocabularyDeckFlashcardDao() {
        return vocabularyDeckFlashcardDao;
    }

    public VocabularyFlashcardDao getVocabularyFlashcardDao() {
        return vocabularyFlashcardDao;
    }

    public VocabularyGeneralDataDao getVocabularyGeneralDataDao() {
        return vocabularyGeneralDataDao;
    }

}
