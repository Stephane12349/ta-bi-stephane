export type UserRole = "BUYER" | "WHOLESALER" | "ADMIN";

export interface UserProfile {
  id: string;
  phone: string;
  fullName: string;
  role: UserRole;
  businessName: string;
  commune: string;
  isVerified: boolean;
  dataSaverEnabled: boolean;
}

export interface PriceTier {
  minQuantity: number;
  maxQuantity: number | null;
  unitPriceFcfa: number;
  label: string;
}

export interface Category {
  id: string;
  name: string;
  slug: string;
  iconKey: string;
  description: string;
  productCount: number;
}

export interface Product {
  id: string;
  name: string;
  description: string;
  categoryId: string;
  sellerId: string;
  sellerName: string;
  sellerSector: string;
  images: string[];
  packaging: string;
  minOrderQuantity: number;
  basePrice: number;
  priceTiers: PriceTier[];
  stockStatus: "IN_STOCK" | "LOW_STOCK" | "OUT_OF_STOCK";
  stockQuantity: number;
  isVerifiedSeller: boolean;
  isPromoted: boolean;
}

export interface WholesalerShop {
  id: string;
  name: string;
  description: string;
  ownerName: string;
  phone: string;
  whatsapp: string;
  address: string;
  marketSector: string;
  landmarks: string;
  hours: string;
  isVerified: boolean;
  verificationBadge: "TERRAIN_VERIFIED" | "CERTIFIED_IMPORTATEUR" | "STANDARD";
  rating: number;
  reviewCount: number;
  transactionCount: number;
  coverImageUrl: string;
}

export interface CartItem {
  product: Product;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export type OrderStatus =
  | "PENDING"
  | "CONFIRMED"
  | "PREPARING"
  | "READY"
  | "COMPLETED"
  | "CANCELLED"
  | "REJECTED";

export interface OrderItem {
  productId: string;
  productName: string;
  packaging: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
}

export interface Order {
  id: string;
  buyerId: string;
  buyerName: string;
  buyerPhone: string;
  sellerId: string;
  sellerName: string;
  sellerSector: string;
  items: OrderItem[];
  totalAmount: number;
  status: OrderStatus;
  pickupPinCode: string;
  deliveryType: "CLICK_AND_COLLECT" | "GARE_EXPEDITION";
  notes: string;
  createdAt: number;
}

export interface AppNotification {
  id: string;
  title: string;
  message: string;
  type: string;
  timestamp: number;
  isRead: boolean;
}

export interface ShopReport {
  id: string;
  shopId: string;
  shopName: string;
  reporterId: string;
  reporterName: string;
  reason: string;
  description: string;
  status: string;
  createdAt: number;
}
