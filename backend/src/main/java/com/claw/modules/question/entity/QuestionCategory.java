package com.claw.modules.question.entity;

import com.claw.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目分类实体类
 * 对应数据库表：question_categories
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "question_categories", 
       indexes = {
           @Index(name = "idx_parent_id", columnList = "parent_id"),
           @Index(name = "idx_slug", columnList = "slug"),
           @Index(name = "idx_sort_order", columnList = "sort_order"),
           @Index(name = "idx_is_active", columnList = "is_active")
       })
public class QuestionCategory extends BaseEntity {

    /**
     * 父分类ID
     */
    @Column(name = "parent_id")
    private Long parentId = 0L;

    /**
     * 分类名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 分类标识
     */
    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    /**
     * 分类描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 图标URL
     */
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    /**
     * 层级
     */
    @Column(name = "level")
    private Integer level = 1;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 是否叶子节点
     */
    @Column(name = "is_leaf")
    private Boolean isLeaf = false;

    /**
     * 题目数量
     */
    @Column(name = "question_count")
    private Integer questionCount = 0;

    /**
     * 学习次数
     */
    @Column(name = "study_count")
    private Integer studyCount = 0;

    /**
     * 是否启用
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

    // 父分类
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private QuestionCategory parent;

    // 子分类
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<QuestionCategory> children = new ArrayList<>();

    // 关联的题目
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    /**
     * 增加题目数量
     */
    public void incrementQuestionCount() {
        this.questionCount = (this.questionCount == null ? 0 : this.questionCount) + 1;
    }

    /**
     * 减少题目数量
     */
    public void decrementQuestionCount() {
        this.questionCount = Math.max(0, (this.questionCount == null ? 0 : this.questionCount) - 1);
    }

    /**
     * 增加学习次数
     */
    public void incrementStudyCount() {
        this.studyCount = (this.studyCount == null ? 0 : this.studyCount) + 1;
    }

    /**
     * 判断是否为根分类
     */
    public boolean isRoot() {
        return parentId == null || parentId == 0L;
    }

    /**
     * 获取完整路径
     */
    public String getFullPath() {
        if (parent == null || isRoot()) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }

    /**
     * 更新叶子节点状态
     */
    public void updateLeafStatus() {
        this.isLeaf = children == null || children.isEmpty();
    }

    /**
     * 添加子分类
     */
    public void addChild(QuestionCategory child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
        child.setParentId(this.getId());
        child.setLevel(this.getLevel() + 1);
        updateLeafStatus();
    }

    /**
     * 移除子分类
     */
    public void removeChild(QuestionCategory child) {
        if (children != null) {
            children.remove(child);
            child.setParent(null);
            child.setParentId(0L);
            child.setLevel(1);
            updateLeafStatus();
        }
    }
}