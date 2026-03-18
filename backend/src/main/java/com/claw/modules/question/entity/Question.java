package com.claw.modules.question.entity;

import com.claw.common.model.BaseEntity;
import com.claw.modules.question.enums.DifficultyLevel;
import com.claw.modules.question.enums.QuestionStatus;
import com.claw.modules.question.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目实体类
 * 对应数据库表：questions
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "questions", 
       indexes = {
           @Index(name = "idx_category_id", columnList = "category_id"),
           @Index(name = "idx_difficulty", columnList = "difficulty"),
           @Index(name = "idx_status", columnList = "status"),
           @Index(name = "idx_hot_score", columnList = "hot_score"),
           @Index(name = "idx_created_at", columnList = "created_at"),
           @Index(name = "idx_published_at", columnList = "published_at")
       })
public class Question extends BaseEntity {

    /**
     * 分类ID
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * 题目标题
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * 题目内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 题目类型
     */
    @Column(name = "question_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    /**
     * 难度级别
     */
    @Column(name = "difficulty", length = 20)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty = DifficultyLevel.MEDIUM;

    /**
     * 标签，JSON数组存储
     */
    @Column(name = "tags", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    /**
     * 考点，JSON数组存储
     */
    @Column(name = "key_points", columnDefinition = "JSON")
    @Convert(converter = StringListConverter.class)
    private List<String> keyPoints;

    /**
     * 选项，JSON数组存储（针对选择题）
     */
    @Column(name = "options", columnDefinition = "JSON")
    private String options;

    /**
     * 正确答案
     */
    @JsonIgnore
    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;

    /**
     * 答案解析
     */
    @Column(name = "answer_explanation", columnDefinition = "TEXT")
    private String answerExplanation;

    /**
     * 代码模板（针对代码题）
     */
    @Column(name = "code_template", columnDefinition = "TEXT")
    private String codeTemplate;

    /**
     * 测试用例，JSON数组存储
     */
    @Column(name = "test_cases", columnDefinition = "JSON")
    private String testCases;

    /**
     * 编程语言
     */
    @Column(name = "language", length = 50)
    private String language;

    /**
     * 来源类型
     */
    @Column(name = "source_type", length = 50)
    private String sourceType;

    /**
     * 来源信息
     */
    @Column(name = "source_info", length = 500)
    private String sourceInfo;

    /**
     * 原始URL
     */
    @Column(name = "original_url", length = 500)
    private String originalUrl;

    /**
     * 浏览次数
     */
    @Column(name = "view_count")
    private Integer viewCount = 0;

    /**
     * 答题次数
     */
    @Column(name = "answer_count")
    private Integer answerCount = 0;

    /**
     * 正确次数
     */
    @Column(name = "correct_count")
    private Integer correctCount = 0;

    /**
     * 收藏次数
     */
    @Column(name = "favorite_count")
    private Integer favoriteCount = 0;

    /**
     * 错误次数
     */
    @Column(name = "wrong_count")
    private Integer wrongCount = 0;

    /**
     * 平均耗时（秒）
     */
    @Column(name = "avg_time_spent")
    private Integer avgTimeSpent;

    /**
     * 热度分数
     */
    @Column(name = "hot_score", precision = 10, scale = 4)
    private Double hotScore = 0.0;

    /**
     * 趋势分数
     */
    @Column(name = "trend_score", precision = 10, scale = 4)
    private Double trendScore = 0.0;

    /**
     * 题目状态
     */
    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private QuestionStatus status = QuestionStatus.DRAFT;

    /**
     * 是否推荐
     */
    @Column(name = "is_recommended")
    private Boolean isRecommended = false;

    /**
     * 是否热门
     */
    @Column(name = "is_hot")
    private Boolean isHot = false;

    /**
     * 审核人ID
     */
    @Column(name = "reviewer_id")
    private Long reviewerId;

    /**
     * 审核时间
     */
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /**
     * 审核意见
     */
    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 发布时间
     */
    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    // 关联的分类
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", insertable = false, updatable = false)
    private QuestionCategory category;

    // 转换器类
    @Converter
    public static class StringListConverter implements AttributeConverter<List<String>, String> {
        @Override
        public String convertToDatabaseColumn(List<String> list) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            return String.join(",", list);
        }

        @Override
        public List<String> convertToEntityAttribute(String dbData) {
            if (dbData == null || dbData.trim().isEmpty()) {
                return List.of();
            }
            return List.of(dbData.split(","));
        }
    }

    /**
     * 增加浏览次数
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
        updateHotScore();
    }

    /**
     * 增加答题次数
     * @param isCorrect 是否答题正确
     * @param timeSpent 答题耗时（秒）
     */
    public void incrementAnswerCount(boolean isCorrect, int timeSpent) {
        this.answerCount = (this.answerCount == null ? 0 : this.answerCount) + 1;
        
        if (isCorrect) {
            this.correctCount = (this.correctCount == null ? 0 : this.correctCount) + 1;
        } else {
            this.wrongCount = (this.wrongCount == null ? 0 : this.wrongCount) + 1;
        }
        
        // 更新平均耗时
        if (this.avgTimeSpent == null) {
            this.avgTimeSpent = timeSpent;
        } else {
            int totalTime = this.avgTimeSpent * (this.answerCount - 1) + timeSpent;
            this.avgTimeSpent = totalTime / this.answerCount;
        }
        
        updateHotScore();
    }

    /**
     * 增加收藏次数
     */
    public void incrementFavoriteCount() {
        this.favoriteCount = (this.favoriteCount == null ? 0 : this.favoriteCount) + 1;
        updateHotScore();
    }

    /**
     * 减少收藏次数
     */
    public void decrementFavoriteCount() {
        this.favoriteCount = Math.max(0, (this.favoriteCount == null ? 0 : this.favoriteCount) - 1);
        updateHotScore();
    }

    /**
     * 更新热度分数
     */
    private void updateHotScore() {
        // 简单的热度计算公式：浏览量 * 0.3 + 答题数 * 0.4 + 收藏数 * 0.3
        double viewWeight = 0.3;
        double answerWeight = 0.4;
        double favoriteWeight = 0.3;
        
        int view = this.viewCount != null ? this.viewCount : 0;
        int answer = this.answerCount != null ? this.answerCount : 0;
        int favorite = this.favoriteCount != null ? this.favoriteCount : 0;
        
        this.hotScore = view * viewWeight + answer * answerWeight + favorite * favoriteWeight;
        
        // 如果热度分数高，标记为热门
        this.isHot = this.hotScore > 50.0;
    }

    /**
     * 计算正确率
     */
    public Double getCorrectRate() {
        if (answerCount == null || answerCount == 0) {
            return 0.0;
        }
        if (correctCount == null) {
            return 0.0;
        }
        return (double) correctCount / answerCount * 100.0;
    }

    /**
     * 发布题目
     */
    public void publish(Long reviewerId, String reviewComment) {
        this.status = QuestionStatus.PUBLISHED;
        this.reviewerId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.reviewComment = reviewComment;
        this.publishedAt = LocalDateTime.now();
        this.isRecommended = false; // 新发布的题目默认不推荐
    }

    /**
     * 下架题目
     */
    public void unpublish(String reason) {
        this.status = QuestionStatus.UNPUBLISHED;
        this.reviewComment = reason;
        this.isRecommended = false;
        this.isHot = false;
    }

    /**
     * 审核通过
     */
    public void approve(Long reviewerId, String comment) {
        this.status = QuestionStatus.PUBLISHED;
        this.reviewerId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.reviewComment = comment;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * 审核拒绝
     */
    public void reject(Long reviewerId, String reason) {
        this.status = QuestionStatus.REJECTED;
        this.reviewerId = reviewerId;
        this.reviewedAt = LocalDateTime.now();
        this.reviewComment = reason;
    }

    /**
     * 判断题目是否已发布
     */
    public boolean isPublished() {
        return this.status == QuestionStatus.PUBLISHED;
    }

    /**
     * 判断题目是否在审核中
     */
    public boolean isUnderReview() {
        return this.status == QuestionStatus.UNDER_REVIEW;
    }

    /**
     * 判断题目是否已下架
     */
    public boolean isUnpublished() {
        return this.status == QuestionStatus.UNPUBLISHED;
    }
}