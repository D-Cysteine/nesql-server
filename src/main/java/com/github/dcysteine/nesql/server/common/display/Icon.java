package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Icon {
    public abstract String getDescription();
    public abstract String getUrl();
    public abstract String getImage();
    @Nullable public abstract String getTopLeft();
    @Nullable public abstract String getBottomRight();
    @Nullable public abstract String getBottomLeftImage();

    public static Builder builder() {
        return new AutoValue_Icon.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDescription(String description);
        public abstract Builder setUrl(String url);
        public abstract Builder setImage(String image);
        public abstract Builder setTopLeft(@Nullable String topLeft);
        public abstract Builder setBottomRight(@Nullable String bottomRight);
        public abstract Builder setBottomLeftImage(@Nullable String bottomLeftImage);

        public abstract Icon build();
    }
}
