package com.github.dcysteine.nesql.server.common.display;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/** Display class used for showing related info in a panel. */
@AutoValue
public abstract class InfoPanel {
    public abstract String getTitle();
    public abstract ImmutableList<Link> getLinks();
    public abstract ImmutableList<Icon> getIcons();
    public abstract ImmutableList<Property> getProperties();

    public static Builder builder() {
        return new AutoValue_InfoPanel.Builder();
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setTitle(String title);
        public abstract ImmutableList.Builder<Link> linksBuilder();
        public abstract ImmutableList.Builder<Icon> iconsBuilder();
        public abstract ImmutableList.Builder<Property> propertiesBuilder();

        public Builder addLink(Link link) {
            linksBuilder().add(link);
            return this;
        }

        public Builder addLink(String text, String url) {
            return addLink(Link.create(text, url));
        }

        public Builder addIcon(Icon icon) {
            iconsBuilder().add(icon);
            return this;
        }

        public Builder addProperty(Property property) {
            propertiesBuilder().add(property);
            return this;
        }

        public Builder addProperty(String key, String value) {
            return addProperty(Property.ofString(key, value));
        }

        public Builder addProperty(String key, boolean value) {
            return addProperty(Property.ofBoolean(key, value));
        }

        public abstract InfoPanel build();
    }
}
