package fezlr.fluffy.feed.dto.request;

import fezlr.fluffy.common.constant.ValidationConstants;
import jakarta.validation.constraints.Size;

public record FeedRequest(
        String photoUrl,

        @Size(min = ValidationConstants.TEXT_SIZE_MIN, max = ValidationConstants.TEXT_SIZE_MAX)
        String text
) {
}
