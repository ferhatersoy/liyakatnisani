package com.example.liyakatnisani.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.liyakatnisani.data.local.dao.LectureNoteDao
import com.example.liyakatnisani.data.local.dao.QuestionDao
import com.example.liyakatnisani.data.local.dao.SubjectDao
import com.example.liyakatnisani.data.local.entity.LectureNoteEntity
import com.example.liyakatnisani.data.local.entity.QuestionEntity
import com.example.liyakatnisani.data.local.entity.SubjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SubjectEntity::class, LectureNoteEntity::class, QuestionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun lectureNoteDao(): LectureNoteDao
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "liyakat_nisani_database"
                )
                .fallbackToDestructiveMigration() // Geliştirme için
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Veritabanı ilk oluşturulduğunda örnek verileri ekle
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(database)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            val subjectDao = database.subjectDao()
            val lectureNoteDao = database.lectureNoteDao()
            val questionDao = database.questionDao()

            // Örnek Konular
            val subject1Id = subjectDao.insert(SubjectEntity(name = "Matematik", description = "Temel Matematik Konuları"))
            val subject2Id = subjectDao.insert(SubjectEntity(name = "Tarih", description = "Osmanlı Tarihi"))

            // Örnek Ders Notları - Matematik
            val lectureNote1MathId = lectureNoteDao.insert(LectureNoteEntity(subjectId = subject1Id, title = "Cebirsel İfadeler", content = "Cebirsel ifadeler ve denklemler..."))
            val lectureNote2MathId = lectureNoteDao.insert(LectureNoteEntity(subjectId = subject1Id, title = "Geometri Temelleri", content = "Açılar, üçgenler ve dörtgenler..."))

            // Örnek Ders Notları - Tarih
            val lectureNote1TarihId = lectureNoteDao.insert(LectureNoteEntity(subjectId = subject2Id, title = "Osmanlı Kuruluş Dönemi", content = "Osmanlı Beyliği'nin kuruluşu ve ilk padişahlar..."))
            val lectureNote2TarihId = lectureNoteDao.insert(LectureNoteEntity(subjectId = subject2Id, title = "Osmanlı Yükselme Dönemi", content = "Fatih Sultan Mehmet, Yavuz Sultan Selim ve Kanuni Sultan Süleyman dönemleri..."))

            // Örnek Sorular - Cebirsel İfadeler
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1MathId, questionText = "2x + 5 = 15 ise x kaçtır?", optionA = "3", optionB = "4", optionC = "5", optionD = "6", correctOption = 3, difficulty = "Kolay", explanation = "Denklem çözümü: 2x = 10, x = 5"))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1MathId, questionText = "(a+b)^2 açılımı nedir?", optionA = "a^2+b^2", optionB = "a^2-2ab+b^2", optionC = "a^2+2ab+b^2", optionD = "a^2-b^2", correctOption = 3, difficulty = "Kolay", explanation = "Tam kare açılımı formülü."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1MathId, questionText = "Bir sayının 3 katının 7 eksiği 14 ise bu sayı kaçtır?", optionA = "7", optionB = "8", optionC = "9", optionD = "10", correctOption = 1, difficulty = "Orta", explanation = "3x - 7 = 14 => 3x = 21 => x = 7"))

            // Örnek Sorular - Geometri Temelleri
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2MathId, questionText = "Bir üçgenin iç açılar toplamı kaçtır?", optionA = "90", optionB = "180", optionC = "270", optionD = "360", correctOption = 2, difficulty = "Kolay", explanation = "Tüm üçgenlerin iç açıları toplamı 180 derecedir."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2MathId, questionText = "Karenin bir iç açısı kaç derecedir?", optionA = "45", optionB = "60", optionC = "90", optionD = "120", correctOption = 3, difficulty = "Kolay", explanation = "Karenin tüm iç açıları 90 derecedir."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2MathId, questionText = "Eşkenar üçgenin bir dış açısı kaç derecedir?", optionA = "60", optionB = "90", optionC = "120", optionD = "150", correctOption = 3, difficulty = "Orta", explanation = "Eşkenar üçgenin bir iç açısı 60 derecedir. Dış açısı 180-60=120 derecedir."))

            // Örnek Sorular - Osmanlı Kuruluş Dönemi
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1TarihId, questionText = "Osmanlı Devleti hangi yıl kurulmuştur?", optionA = "1299", optionB = "1071", optionC = "1453", optionD = "1326", correctOption = 1, difficulty = "Kolay", explanation = "Genel kabul gören tarih 1299'dur."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1TarihId, questionText = "Osmanlı Devleti'nin ilk başkenti neresidir?", optionA = "İstanbul", optionB = "Edirne", optionC = "Bursa", optionD = "Söğüt", correctOption = 4, difficulty = "Orta", explanation = "İlk merkez Söğüt ve Domaniç'tir, ardından Bursa başkent olmuştur."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote1TarihId, questionText = "Koyunhisar Savaşı hangi padişah döneminde yapılmıştır?", optionA = "Orhan Bey", optionB = "Osman Bey", optionC = "I. Murad", optionD = "Yıldırım Bayezid", correctOption = 2, difficulty = "Zor", explanation = "Koyunhisar Savaşı (Bafeus Muharebesi) Osman Bey döneminde Bizans'a karşı yapılmıştır."))

            // Örnek Sorular - Osmanlı Yükselme Dönemi
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2TarihId, questionText = "İstanbul hangi padişah tarafından fethedilmiştir?", optionA = "Yavuz Sultan Selim", optionB = "Kanuni Sultan Süleyman", optionC = "Fatih Sultan Mehmet", optionD = "II. Bayezid", correctOption = 3, difficulty = "Kolay", explanation = "İstanbul, 1453 yılında Fatih Sultan Mehmet tarafından fethedilmiştir."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2TarihId, questionText = "Mısır Seferi hangi padişah döneminde yapılmıştır?", optionA = "Fatih Sultan Mehmet", optionB = "Yavuz Sultan Selim", optionC = "Kanuni Sultan Süleyman", optionD = "I. Selim", correctOption = 2, difficulty = "Orta", explanation = "Mısır Seferi, Yavuz Sultan Selim (I. Selim) tarafından yapılmış ve Hilafet Osmanlı'ya geçmiştir."))
            questionDao.insert(QuestionEntity(lectureNoteId = lectureNote2TarihId, questionText = "Preveze Deniz Savaşı'nda Osmanlı Donanması'nın komutanı kimdir?", optionA = "Piri Reis", optionB = "Turgut Reis", optionC = "Barbaros Hayreddin Paşa", optionD = "Oruç Reis", correctOption = 3, difficulty = "Zor", explanation = "Preveze Deniz Savaşı'nda Osmanlı Donanması'na Barbaros Hayreddin Paşa komuta etmiştir."))
        }
    }
}
