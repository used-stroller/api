package team.three.usedstroller.api.utils;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.three.usedstroller.api.error.ApiErrorCode;

@Data
@Builder
public class ResponseListDto<T> {
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final int status;

    private final String error;

    private final String message;

    private final Object meta;

    private final ResponseListDataDto<T> data;

    public static <T> ResponseEntity<ResponseListDto<T>> toResponseEntity(Page<T> list) {
        return ResponseEntity
                .status(ApiErrorCode.SUCCESS.getStatus())
                .body(ResponseListDto.<T>builder()
                        .status(HttpStatus.OK.value())
                        .error(ApiErrorCode.SUCCESS.getCode())
                        .message("")
                        .data(toResponseListDataDto(list))
                        .build()
                );
    }

    public static <T> ResponseEntity<ResponseListDto<T>> toResponseEntity(List<T> list) {
        return ResponseEntity
                .status(ApiErrorCode.SUCCESS.getStatus())
                .body(ResponseListDto.<T>builder()
                        .status(HttpStatus.OK.value())
                        .error(ApiErrorCode.SUCCESS.getCode())
                        .message("")
                        .data(toResponseListDataDto(list))
                        .build()
                );
    }

    public static <T> ResponseEntity<ResponseListDto<T>> toResponseEntity(Page<T> list, Object meta) {
        return ResponseEntity
                .status(ApiErrorCode.SUCCESS.getStatus())
                .body(ResponseListDto.<T>builder()
                        .status(HttpStatus.OK.value())
                        .error(ApiErrorCode.SUCCESS.getCode())
                        .message("")
                        .meta(meta)
                        .data(toResponseListDataDto(list))
                        .build()
                );
    }

    public static <T> ResponseListDataDto<T> toResponseListDataDto(Page<T> list) {
        int page = list.getPageable().getPageNumber() + 1;
        int pageStart = Math.max(page - 4, 1);
        int pageEnd = Math.min(page + 9, list.getTotalPages());
        Long totalElements = list.getTotalElements();
        int totalPage = list.getTotalPages();

        return ResponseListDataDto.<T>builder()
                .page(page)
                .pageStart(pageStart)
                .pageEnd(pageEnd)
                .totalElements(totalElements)
                .totalPage(totalPage)
                .contents(list.getContent())
                .build();
    }

    public static <T> ResponseListDataDto<T> toResponseListDataDto(List<T> list) {
        int page = 0;
        int pageStart = 0;
        int pageEnd = 0;
        Long totalElements = (long) list.size();
        int totalPage = 0;

        return ResponseListDataDto.<T>builder()
                .page(page)
                .pageStart(pageStart)
                .pageEnd(pageEnd)
                .totalElements(totalElements)
                .totalPage(totalPage)
                .contents(list)
                .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseListDataDto<T> {
        @NotNull
        private int page;

        @NotNull
        private int pageStart;

        @NotNull
        private int pageEnd;

        @NotNull
        private Long totalElements;

        @NotNull
        private int totalPage;

        private List<T> contents;
    }
}
