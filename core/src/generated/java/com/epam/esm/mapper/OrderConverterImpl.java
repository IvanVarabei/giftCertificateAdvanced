package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderItemDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-02T20:39:13+0300",
    comments = "version: 1.4.1.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-6.7.1.jar, environment: Java 14.0.1 (Oracle Corporation)"
)
@Component
public class OrderConverterImpl implements OrderConverter {

    @Override
    public OrderDto toDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setUser( userToUserDto( order.getUser() ) );
        orderDto.setOrderItems( orderItemListToOrderItemDtoList( order.getOrderItems() ) );
        orderDto.setCost( order.getCost() );
        orderDto.setCreatedDate( order.getCreatedDate() );

        return orderDto;
    }

    @Override
    public Order toEntity(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        Order order = new Order();

        order.setId( orderDto.getId() );
        order.setUser( userDtoToUser( orderDto.getUser() ) );
        order.setCreatedDate( orderDto.getCreatedDate() );
        order.setOrderItems( toEntities( orderDto.getOrderItems() ) );
        order.setCost( orderDto.getCost() );

        return order;
    }

    @Override
    public List<OrderItem> toEntities(List<OrderItemDto> orderItemDtoList) {
        if ( orderItemDtoList == null ) {
            return null;
        }

        List<OrderItem> list = new ArrayList<OrderItem>( orderItemDtoList.size() );
        for ( OrderItemDto orderItemDto : orderItemDtoList ) {
            list.add( orderItemDtoToOrderItem( orderItemDto ) );
        }

        return list;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();

        UserDto userDto = new UserDto( id, username, email );

        return userDto;
    }

    protected TagDto tagToTagDto(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDto tagDto = new TagDto();

        tagDto.setId( tag.getId() );
        tagDto.setName( tag.getName() );

        return tagDto;
    }

    protected List<TagDto> tagListToTagDtoList(List<Tag> list) {
        if ( list == null ) {
            return null;
        }

        List<TagDto> list1 = new ArrayList<TagDto>( list.size() );
        for ( Tag tag : list ) {
            list1.add( tagToTagDto( tag ) );
        }

        return list1;
    }

    protected OrderItemDto orderItemToOrderItemDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDto orderItemDto = new OrderItemDto();

        orderItemDto.setId( orderItem.getId() );
        orderItemDto.setName( orderItem.getName() );
        orderItemDto.setDescription( orderItem.getDescription() );
        orderItemDto.setPrice( orderItem.getPrice() );
        orderItemDto.setDuration( orderItem.getDuration() );
        orderItemDto.setTags( tagListToTagDtoList( orderItem.getTags() ) );
        orderItemDto.setQuantity( orderItem.getQuantity() );
        orderItemDto.setCertificateId( orderItem.getCertificateId() );

        return orderItemDto;
    }

    protected List<OrderItemDto> orderItemListToOrderItemDtoList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemDto> list1 = new ArrayList<OrderItemDto>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemDto( orderItem ) );
        }

        return list1;
    }

    protected User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDto.getId() );
        user.setUsername( userDto.getUsername() );
        user.setEmail( userDto.getEmail() );

        return user;
    }

    protected Tag tagDtoToTag(TagDto tagDto) {
        if ( tagDto == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setId( tagDto.getId() );
        tag.setName( tagDto.getName() );

        return tag;
    }

    protected List<Tag> tagDtoListToTagList(List<TagDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Tag> list1 = new ArrayList<Tag>( list.size() );
        for ( TagDto tagDto : list ) {
            list1.add( tagDtoToTag( tagDto ) );
        }

        return list1;
    }

    protected OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        if ( orderItemDto == null ) {
            return null;
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setId( orderItemDto.getId() );
        orderItem.setName( orderItemDto.getName() );
        orderItem.setDescription( orderItemDto.getDescription() );
        orderItem.setPrice( orderItemDto.getPrice() );
        orderItem.setDuration( orderItemDto.getDuration() );
        orderItem.setTags( tagDtoListToTagList( orderItemDto.getTags() ) );
        orderItem.setQuantity( orderItemDto.getQuantity() );
        orderItem.setCertificateId( orderItemDto.getCertificateId() );

        return orderItem;
    }
}
