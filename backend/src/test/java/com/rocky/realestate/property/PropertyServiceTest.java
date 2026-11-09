package com.rocky.realestate.property;
import org.junit.jupiter.api.Test; import java.math.BigDecimal; import java.util.UUID; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
class PropertyServiceTest {
 @Test void createsPropertyPendingApproval(){PropertyRepository repo=mock(PropertyRepository.class); when(repo.save(any(Property.class))).thenAnswer(i->i.getArgument(0)); PropertyService service=new PropertyService(repo); PropertyRequest request=new PropertyRequest(UUID.randomUUID(),"Family home","Near park",PropertyCategory.HOUSE,ListingType.SALE,new BigDecimal("100000"),"Dhaka","Dhanmondi",new BigDecimal("1500"),3,2); PropertyResponse response=service.create(request); assertEquals(PropertyStatus.PENDING_APPROVAL,response.status()); verify(repo).save(any(Property.class));}
}
