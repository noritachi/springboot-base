package com.base.auth.mapper;

import com.base.auth.dto.article.ArticleAdminDto;
import com.base.auth.form.article.CreateArticleForm;
import com.base.auth.form.article.UpdateArticleForm;
import com.base.auth.model.Article;
import com.base.auth.model.Category;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-02T13:33:53+0700",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.20 (Oracle Corporation)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public Article fromCreateArticleFormToEntity(CreateArticleForm createArticleForm) {
        if ( createArticleForm == null ) {
            return null;
        }

        Article article = new Article();

        article.setCategory( createArticleFormToCategory( createArticleForm ) );
        article.setKind( createArticleForm.getKind() );
        article.setBanner( createArticleForm.getBanner() );
        article.setDescription( createArticleForm.getDescription() );
        article.setAvatar( createArticleForm.getAvatar() );
        article.setTitle( createArticleForm.getTitle() );
        article.setContent( createArticleForm.getContent() );
        article.setPinTop( createArticleForm.getPinTop() );
        article.setStatus( createArticleForm.getStatus() );

        return article;
    }

    @Override
    public void fromUpdateArticleFormToEntity(UpdateArticleForm updateArticleForm, Article article) {
        if ( updateArticleForm == null ) {
            return;
        }

        if ( updateArticleForm.getBanner() != null ) {
            article.setBanner( updateArticleForm.getBanner() );
        }
        if ( updateArticleForm.getDescription() != null ) {
            article.setDescription( updateArticleForm.getDescription() );
        }
        if ( updateArticleForm.getAvatar() != null ) {
            article.setAvatar( updateArticleForm.getAvatar() );
        }
        if ( updateArticleForm.getTitle() != null ) {
            article.setTitle( updateArticleForm.getTitle() );
        }
        if ( updateArticleForm.getContent() != null ) {
            article.setContent( updateArticleForm.getContent() );
        }
        if ( updateArticleForm.getPinTop() != null ) {
            article.setPinTop( updateArticleForm.getPinTop() );
        }
        if ( updateArticleForm.getId() != null ) {
            article.setId( updateArticleForm.getId() );
        }
        if ( updateArticleForm.getStatus() != null ) {
            article.setStatus( updateArticleForm.getStatus() );
        }
    }

    @Override
    public ArticleAdminDto fromEntityToArticleDtoNoArticleContent(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleAdminDto articleAdminDto = new ArticleAdminDto();

        articleAdminDto.setKind( article.getKind() );
        articleAdminDto.setBanner( article.getBanner() );
        articleAdminDto.setDescription( article.getDescription() );
        articleAdminDto.setAvatar( article.getAvatar() );
        articleAdminDto.setTitle( article.getTitle() );
        if ( article.getCreatedDate() != null ) {
            articleAdminDto.setCreatedDate( LocalDateTime.ofInstant( article.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        articleAdminDto.setCreatedBy( article.getCreatedBy() );
        if ( article.getModifiedDate() != null ) {
            articleAdminDto.setModifiedDate( LocalDateTime.ofInstant( article.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        articleAdminDto.setPinTop( article.getPinTop() );
        articleAdminDto.setModifiedBy( article.getModifiedBy() );
        articleAdminDto.setId( article.getId() );
        articleAdminDto.setCategoryId( articleCategoryId( article ) );
        articleAdminDto.setStatus( article.getStatus() );

        return articleAdminDto;
    }

    @Override
    public List<ArticleAdminDto> fromEntityListToArticleDtoListNoArticleContent(List<Article> article) {
        if ( article == null ) {
            return null;
        }

        List<ArticleAdminDto> list = new ArrayList<ArticleAdminDto>( article.size() );
        for ( Article article1 : article ) {
            list.add( fromEntityToArticleDtoNoArticleContent( article1 ) );
        }

        return list;
    }

    @Override
    public ArticleAdminDto fromEntityToArticleDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleAdminDto articleAdminDto = new ArticleAdminDto();

        articleAdminDto.setKind( article.getKind() );
        articleAdminDto.setBanner( article.getBanner() );
        articleAdminDto.setDescription( article.getDescription() );
        articleAdminDto.setAvatar( article.getAvatar() );
        articleAdminDto.setTitle( article.getTitle() );
        articleAdminDto.setContent( article.getContent() );
        if ( article.getCreatedDate() != null ) {
            articleAdminDto.setCreatedDate( LocalDateTime.ofInstant( article.getCreatedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        articleAdminDto.setCreatedBy( article.getCreatedBy() );
        if ( article.getModifiedDate() != null ) {
            articleAdminDto.setModifiedDate( LocalDateTime.ofInstant( article.getModifiedDate().toInstant(), ZoneId.of( "UTC" ) ) );
        }
        articleAdminDto.setPinTop( article.getPinTop() );
        articleAdminDto.setModifiedBy( article.getModifiedBy() );
        articleAdminDto.setId( article.getId() );
        articleAdminDto.setCategoryId( articleCategoryId( article ) );
        articleAdminDto.setStatus( article.getStatus() );

        return articleAdminDto;
    }

    protected Category createArticleFormToCategory(CreateArticleForm createArticleForm) {
        if ( createArticleForm == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( createArticleForm.getCategoryId() );

        return category;
    }

    private Long articleCategoryId(Article article) {
        if ( article == null ) {
            return null;
        }
        Category category = article.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
